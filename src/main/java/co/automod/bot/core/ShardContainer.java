package co.automod.bot.core;

import co.automod.bot.Config;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 *
 */
public class ShardContainer {

    private final Shard[] shards;

    public ShardContainer() {
        shards = new Shard[getRecommendedShards()];
        startShards();
    }

    private void startShards() {
        for (int i = 0; i < shards.length; i++) {
            shards[i] = new Shard(this);
            boolean success = false;
            while (!success) {
                try {
                    shards[i].reboot(i, shards.length);
                    success = true;
                    Thread.sleep(Config.SHARD_RATELIMIT);
                } catch (RateLimitedException | InterruptedException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(Config.SHARD_RATELIMIT);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    /**
     * Retrieves the shard recommendation from discord
     *
     * @return recommended shard count
     */
    public int getRecommendedShards() {
        try {
            HttpResponse<JsonNode> request = Unirest.get("https://discordapp.com/api/gateway/bot")
                    .header("Authorization", "Bot " + Config.discord_token)
                    .header("Content-Type", "application/json")
                    .asJson();
            return Integer.parseInt(request.getBody().getObject().get("shards").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
