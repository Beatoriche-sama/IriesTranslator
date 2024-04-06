package UI.main_dialog;

import configs.Configs;
import translate.LANG;
import translate.SERVICE;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainDialogMenuBar extends JMenuBar {
    private final Configs configs;

    public MainDialogMenuBar(Configs configs) {
        this.configs = configs;

        //JMenu keysMenu = new JMenu("Key input");
        JMenu servicesMenu = new JMenu("Translate service");
        JMenu languagesMenu = new JMenu("Target language");

        JMenu[] menus = new JMenu[]{servicesMenu, languagesMenu};
        Arrays.stream(menus).forEach(this::add);


        //keysMenu.add(createKeyInputItem(service));
        SERVICE[] services = SERVICE.values();
        Arrays.stream(services).forEach(service
                -> servicesMenu.add(createServiceCheckbox(service)));

        LANG[] languages = LANG.values();
        Arrays.stream(languages).forEach(lang
                -> languagesMenu.add(createLangCheckbox(lang)));
    }


    /*private JMenuItem createKeyInputItem(SERVICE service){
        JMenuItem keyItem = new JMenuItem(
                service.getServiceName() + " key");
        keyItem.addActionListener(e -> {
            String key = JOptionPane.showInputDialog(container,
                    "Enter your key",
                    "Key input (￣ω￣)",
                    JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(container,
                    "Your key is: " + key);
            configs.changeServiceKey(service, key);
        });

        return  keyItem;
    }*/

    private JCheckBoxMenuItem createLangCheckbox(LANG lang) {
        JCheckBoxMenuItem langCheckbox = new JCheckBoxMenuItem(lang.getFullName());
        langCheckbox.addItemListener(e -> configs.setTargetLanguage(lang));
        if (lang == configs.getTargetLanguage())
            langCheckbox.setState(true);
        return langCheckbox;
    }

    private JCheckBoxMenuItem createServiceCheckbox(SERVICE service) {
        JCheckBoxMenuItem serviceCheckbox
                = new JCheckBoxMenuItem(service.getServiceName());
        serviceCheckbox.addItemListener(e -> configs.setCurrentService(service));
        if (service == configs.getCurrentService())
            serviceCheckbox.setState(true);
        return serviceCheckbox;
    }

}
