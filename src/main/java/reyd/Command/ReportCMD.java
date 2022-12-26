package reyd.Command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;
import reyd.ReportMain;

public class ReportCMD extends Command {

    public ReportCMD(){
        super("report", ReportMain.getInstance().getConfig().getString("Report.description"));
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET),
        });
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        if (commandSender instanceof Player){
            Player player = (Player) commandSender;

            if (args.length != 1){

                player.sendMessage(ReportMain.getInstance().getConfig().getString("Report.usage"));
                return true;
            }

            try {
                Player reportPLAYER = Server.getInstance().getPlayer(args[0]);

                FormWindowSimple formWindowSimple = new FormWindowSimple(ReportMain.getInstance().getConfig().getString("Form.title").replace("%t%", reportPLAYER.getName()), ReportMain.getInstance().getConfig().getString("Form.content"));
                formWindowSimple.addButton(new ElementButton(ReportMain.getInstance().getConfig().getString("Form.button1")));
                formWindowSimple.addButton(new ElementButton(ReportMain.getInstance().getConfig().getString("Form.button2")));
                formWindowSimple.addButton(new ElementButton(ReportMain.getInstance().getConfig().getString("Form.button3")));
                player.showFormWindow(formWindowSimple, 654321);

                ReportMain.getInstance().reportMap.put(player.getName(), reportPLAYER.getName());
            } catch (NullPointerException e){
                player.sendMessage(ReportMain.getInstance().getConfig().getString("Report.playerNotFound").replace("%t%", args[0]));
            }


        }

        return true;
    }
}
