package site.siredvin.lib.util

import com.mojang.authlib.GameProfile
import dan200.computercraft.ComputerCraft
import dan200.computercraft.api.turtle.FakePlayer
import dan200.computercraft.shared.TurtlePermissions
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stat
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import site.siredvin.lib.ext.toVec3
import site.siredvin.turtlematic.Turtlematic
import java.lang.ref.WeakReference
import java.util.*
import java.util.function.Predicate

class LibFakePlayer(
    level: ServerLevel, owner: Entity?, profile: GameProfile?,
    private val range: Double = 4.0
): FakePlayer(
    level,
    if (profile != null && profile.isComplete) profile else PROFILE) {
    companion object {
        val PROFILE = GameProfile(UUID.fromString("6e483f02-30db-4454-b612-3a167614b276"), "[" + Turtlematic.MOD_ID + "]")
        private val collidablePredicate = EntitySelector.NO_SPECTATORS
    }

    private val owner: WeakReference<Entity>?
    private var digPosition: BlockPos? = null
    private var digBlock: Block? = null
    private var currentDamage = 0f

    init {
        if (owner != null) {
            customName = owner.name
            this.owner = WeakReference(owner)
        } else {
            this.owner = null
        }
    }

    override fun awardStat(stat: Stat<*>) {
        val server = level.server
        if (server != null && gameProfile !== PROFILE) {
            val player: Player? = server.playerList.getPlayer(getUUID())
            player?.awardStat(stat)
        }
    }

    override fun canAttack(p_213336_1_: LivingEntity): Boolean {
        return true
    }

    override fun openTextEdit(p_175141_1_: SignBlockEntity) {}


    override fun isSilent(): Boolean {
        return true
    }

    override fun playSound(soundIn: SoundEvent, volume: Float, pitch: Float) {}

    private fun setState(block: Block?, pos: BlockPos?) {
        if (digPosition != null) {
            gameMode.handleBlockBreakAction(
                digPosition!!,
                ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK,
                Direction.EAST,
                1
            )
        }
        digPosition = pos
        digBlock = block
        currentDamage = 0f
    }

    override fun getEyeHeight(pose: Pose): Float {
        return 0f;
    }

    fun isBlockProtected(pos: BlockPos, state: BlockState): Boolean {
        if (!ComputerCraft.turtlesObeyBlockProtection)
            return false
        if(!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, this, pos, state, null ))
            return true
        if(!TurtlePermissions.isBlockEditable(level, pos, this ))
            return true
        return false
    }

    fun findHit(skipEntity: Boolean, skipBlock: Boolean): HitResult {
        return findHit(skipEntity, skipBlock, null)
    }

    fun findHit(skipEntity: Boolean, skipBlock: Boolean, entityFilter: Predicate<Entity>?): HitResult {
        val origin = Vec3(x, y, z)
        val look = lookAngle
        val target = Vec3(origin.x + look.x * range, origin.y + look.y * range, origin.z + look.z * range)
        val traceContext = ClipContext(origin, target, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)
        val directionVec = traceContext.from.subtract(traceContext.to)
        val traceDirection = Direction.getNearest(directionVec.x, directionVec.y, directionVec.z)
        val blockHit: HitResult = if (skipBlock) {
            BlockHitResult.miss(traceContext.to, traceDirection, BlockPos(traceContext.to))
        } else {
            BlockGetter.traverseBlocks(traceContext.from, traceContext.to, traceContext,
                { _: ClipContext?, blockPos: BlockPos ->
                    if (level.isEmptyBlock(blockPos)) {
                        return@traverseBlocks null
                    }
                    BlockHitResult(
                        Vec3(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                        traceDirection,
                        blockPos,
                        false
                    )
                }
            ) { rayTraceContext: ClipContext ->
                BlockHitResult.miss(
                    rayTraceContext.to,
                    traceDirection,
                    BlockPos(rayTraceContext.to)
                )
            }!!
        }
        if (skipEntity) {
            return blockHit
        }
        val entities = level.getEntities(
            this, boundingBox.expandTowards(look.x * range, look.y * range, look.z * range).inflate(1.0, 1.0, 1.0),
            collidablePredicate
        )
        var closestEntity: LivingEntity? = null
        var closestVec: Vec3? = null
        var closestDistance = range
        for (entityHit in entities) {
            if (entityHit !is LivingEntity) continue
            if (entityFilter != null && !entityFilter.test(entityHit)) continue
            // Add litter bigger that just pick radius
            val box = entityHit.getBoundingBox().inflate(entityHit.getPickRadius() + 0.5)
            val clipResult = box.clip(origin, target)
            if (box.contains(origin)) {
                if (closestDistance >= 0.0) {
                    closestEntity = entityHit
                    closestVec = clipResult.orElse(origin)
                    closestDistance = 0.0
                }
            } else if (clipResult.isPresent) {
                val clipVec = clipResult.get()
                val distance = origin.distanceTo(clipVec)
                if (distance < closestDistance || closestDistance == 0.0) {
                    if (entityHit === entityHit.getRootVehicle()) {
                        if (closestDistance == 0.0) {
                            closestEntity = entityHit
                            closestVec = clipVec
                        }
                    } else {
                        closestEntity = entityHit
                        closestVec = clipVec
                        closestDistance = distance
                    }
                }
            }
        }
        return if (closestEntity != null && closestDistance <= range && (blockHit.type == HitResult.Type.MISS || distanceToSqr(
                blockHit.location
            ) > closestDistance * closestDistance)
        ) {
            EntityHitResult(closestEntity, closestVec)
        } else {
            blockHit
        }
    }

    fun useOnSpecificEntity(entity: Entity, result: HitResult): InteractionResult {
        val simpleInteraction = interactOn(entity, InteractionHand.MAIN_HAND)
        if (simpleInteraction == InteractionResult.SUCCESS)
            return simpleInteraction
        return UseEntityCallback.EVENT.invoker().interact(this, level, InteractionHand.MAIN_HAND, entity, result as EntityHitResult)
    }

    fun use(skipEntity: Boolean, skipBlock: Boolean, entityFilter: Predicate<Entity>?): InteractionResult {
        val hit = findHit(skipEntity, skipBlock, entityFilter);
        if (hit is BlockHitResult) {
            this.interactAt(this, hit.blockPos.toVec3(), InteractionHand.MAIN_HAND)
            level.destroyBlockProgress(id, hit.blockPos, -1)
            return gameMode.useItemOn(this, level, mainHandItem, InteractionHand.MAIN_HAND, hit)
        }
        if (hit is EntityHitResult) {
            return useOnSpecificEntity(hit.entity, hit);
        }
        return InteractionResult.FAIL;
    }

    fun use(skipEntity: Boolean, skipBlock: Boolean): InteractionResult {
        return use(skipEntity, skipBlock, null)
    }

    fun useOnBlock(): InteractionResult {
        return use(true, skipBlock = false)
    }

    fun useOnEntity(): InteractionResult {
        return use(false, skipBlock = true)
    }

    fun useOnFilteredEntity(filter: Predicate<Entity>?): InteractionResult {
        return use(false, skipBlock = true, entityFilter = filter)
    }

    fun digBlock(direction: Direction): Pair<Boolean, String> {
        val hit = findHit(skipEntity = true, skipBlock = false);
        if (hit.type == HitResult.Type.MISS)
            return Pair.of(false, "Nothing to break")
        val pos = BlockPos(hit.location.x, hit.location.y, hit.location.z)
        val state = level.getBlockState(pos)
        val block = state.block
        val tool = inventory.getSelected()
        if (tool.isEmpty)
            return Pair.of(false, "Cannot dig without tool");
        if (block != digBlock || pos != digPosition)
            setState(block, pos)
        if (!level.isEmptyBlock(pos) && !state.material.isLiquid) {
            if (isBlockProtected(pos, state))
                return Pair.of(false, "Cannot break protected block")
            if (block == Blocks.BEDROCK || state.getDestroySpeed(level, pos) <= -1f)
                return Pair.of(false, "Unbreakable block detected")
            if (tool.item !is DiggerItem)
                return Pair.of(false, "Item should be digger tool")
            if (!tool.item.isCorrectToolForDrops(state))
                return Pair.of(false, "Tool cannot mine this block")
            val breakSpeed = 0.5f * tool.getDestroySpeed(state) / state.getDestroySpeed(level, pos) - 0.1f
            for (i in 0..9) {
                currentDamage += breakSpeed
                level.destroyBlockProgress(id, pos, i)

                if (currentDamage > 9) {
                    level.playSound(null, pos, state.soundType.breakSound, SoundSource.NEUTRAL, .25f, 1f)
                    gameMode.handleBlockBreakAction(pos, ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, direction.opposite, 1)
                    gameMode.destroyBlock(pos)
                    level.destroyBlockProgress(id, pos, -1)
                    setState(null, null)
                    break
                }
            }
            return Pair.of(true, "block");
        }
        return Pair.of(false, "Nothing to dig here");
    }
}