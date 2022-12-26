package reyd.Listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.window.FormWindowSimple;
import reyd.ReportMain;

public class ReportListener implements Listener {

    @EventHandler
    public void onFormListener(PlayerFormRespondedEvent event) {

        if (event.getWindow() instanceof FormWindowSimple) {

            if (event.getFormID() == 654321){

                if (event.wasClosed()) {
                    ReportMain.getInstance().reportMap.remove(event.getPlayer().getName());
                    return;
                }

                FormWindowSimple formWindowSimple = (FormWindowSimple) event.getWindow();

                if (formWindowSimple.getResponse().getClickedButtonId() == 0){
                    ReportMain.getInstance().sendReport(ReportMain.getInstance().getConfig().getString("Report.reportMessage").replace("%v%", ReportMain.getInstance().reportMap.get(event.getPlayer().getName())).replace("%p%", event.getPlayer().getName()).replace("%r%", ReportMain.getInstance().getConfig().getString("Report.button1Text")));
                } else if (formWindowSimple.getResponse().getClickedButtonId() == 1){
                    ReportMain.getInstance().sendReport(ReportMain.getInstance().getConfig().getString("Report.reportMessage").replace("%v%", ReportMain.getInstance().reportMap.get(event.getPlayer().getName())).replace("%p%", event.getPlayer().getName()).replace("%r%", ReportMain.getInstance().getConfig().getString("Report.button2Text")));
                } else {
                    ReportMain.getInstance().sendReport(ReportMain.getInstance().getConfig().getString("Report.reportMessage").replace("%v%", ReportMain.getInstance().reportMap.get(event.getPlayer().getName())).replace("%p%", event.getPlayer().getName()).replace("%r%", ReportMain.getInstance().getConfig().getString("Report.button3Text")));
                }

                event.getPlayer().sendMessage(ReportMain.getInstance().getConfig().getString("Report.successSendReport"));

            }

        }
    }

}
