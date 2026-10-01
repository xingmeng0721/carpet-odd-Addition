package carpetodd.xm.command;

import carpet.utils.Messenger;
import carpetodd.xm.CarpetOddSettings;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VillagerCommand {

    public static int unbindBed(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (!CarpetOddSettings.villagerBedUnbind) {
            Messenger.m(source, "r villagerBedUnbind rule is not enabled, use /carpet villagerBedUnbind true");
            return 0;
        }

        int distance = IntegerArgumentType.getInteger(context, "distance");
        ServerLevel level = source.getLevel();
        Vec3 pos = source.getPosition();
        AABB area = new AABB(pos, pos).inflate(distance);

        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, area);

        int count = 0;
        for (Villager villager : villagers) {
            if (!villager.getBrain().hasMemoryValue(MemoryModuleType.HOME)) continue;
            villager.releasePoi(MemoryModuleType.HOME);
            villager.getBrain().eraseMemory(MemoryModuleType.HOME);
            count++;
        }

        if (count > 0) {
            Messenger.m(source, "g Unbound " + count + " villager bed(s)");
        } else {
            Messenger.m(source, "c No bound villagers found within " + distance + " block(s)");
        }

        return count;
    }
}
