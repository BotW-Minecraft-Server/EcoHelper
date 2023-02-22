package link.botwmcs.samchai.ecohelper.impl.cmi;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import link.botwmcs.samchai.ecohelper.config.EcoHelperConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.system.CallbackI;

import java.sql.*;
import java.util.Objects;

public class SqlExecutor {
    private static Connection connect() {
        Connection connection = null;
        String url = null;
        if (Objects.equals(EcoHelperConfig.CONFIG.bukkit_economy_system.get(), "CMI")) {
            url = "jdbc:sqlite:plugins/CMI/cmi.sqlite.db";
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException throwables) {
                EcoHelper.LOGGER.error("Error while connecting to CMI SQLITE FILE", throwables);
            }
        } else {
            return null;
        }
        return connection;
    }


    // DEBUG: CMI will protect SQL when we read it after the first time.
    // SOLUTION: Need just read one time.
    public synchronized void selectPlayerBalance(ServerPlayer player) {
        String playerUUID = player.getStringUUID();
        String selectTarget = "SELECT * FROM users WHERE player_uuid = '" + playerUUID + "'";
        try {
            Connection connection = SqlExecutor.connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectTarget);
            EcoHelper.LOGGER.info("Player: " + resultSet.getString("username") + " Balance: " + resultSet.getDouble("Balance"));
            CmiToEcoHelper.setPlayerBalance(player, resultSet.getDouble("Balance"));
            resultSet.close();
        } catch (SQLException throwables) {
            EcoHelper.LOGGER.error("Error while reading SQL (CMI SQLITE FILE)", throwables);
        }
    }

    public static void syncCmiBalance(ServerPlayer serverPlayer) {
        new Thread(() -> {
            final SqlExecutor sqlExecutor = new SqlExecutor();
            sqlExecutor.selectPlayerBalance(serverPlayer);
        }).start();
    }
}
