package reyd;

import cn.nukkit.plugin.PluginBase;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import reyd.Command.ReportCMD;
import reyd.Listener.ReportListener;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class ReportMain extends PluginBase {

    private static ReportMain instance;
    public static JDA jda;
    public HashMap<String, String> reportMap = new HashMap<>();

    // we will use this for check how to send report
    // true = vkontakte
    // false = discord
    private boolean status;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();

    }

    @Override
    public void onEnable() {

        if (getConfig().getString("Send").equals("vkontakte")){

            // set status
            status = true;

            // register
            getServer().getCommandMap().register("help", new ReportCMD());
            getServer().getPluginManager().registerEvents(new ReportListener(), this);

        } else if (getConfig().getString("Send").equals("discord")){

            // discord
            try {
                jda = JDABuilder.createDefault(getConfig().getString("discord.token")).build();
                jda.awaitReady();
            } catch (InterruptedException | LoginException e) {
                throw new RuntimeException(e);
            }

            // set status
            status = false;

            // register
            getServer().getCommandMap().register("help", new ReportCMD());
            getServer().getPluginManager().registerEvents(new ReportListener(), this);

        } else {
            // warning
            getInstance().getLogger().alert("You need to choose a method to send the report in configs");
        }

        // standard
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /*
    *
    * REPORT API
    *
    * */

    public void sendReport(String reportText){

        if (status){ // vkontakte

            sendReportVK(reportText);

        } else { // discord

            sendReportDiscord(reportText);

        }

    }

    /*
     *
     * Bugs Reporter Utils
     *
     * */

    private void sendReportVK(String reportText) {
        try {
            String Url = "https://api.vk.com/method/messages.send?&random_id=0&peer_id=" + 2000000001 + "&message=" + encode(reportText) + "&access_token=" + getInstance().getConfig().getString("vkontakte.token") + "&v=5.131";
            URL Obj = new URL(Url);
            HttpURLConnection con = (HttpURLConnection)Obj.openConnection();
            con.setRequestMethod("GET");
            BufferedReader inLong = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while(inLong.ready()) {
                con.disconnect();
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendReportDiscord(String reportText) {
        if (jda != null) {
            TextChannel channel = jda.getTextChannelById(getConfig().getString("discord.textChannelById"));
            if (channel != null) {
                channel.sendMessage(reportText).queue();
            }
        }
    }

    public static ReportMain getInstance() {
        return instance;
    }
}
