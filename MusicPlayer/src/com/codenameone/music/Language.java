package com.codenameone.music;

import com.codename1.io.Storage;
import com.codename1.ui.Display;
import com.codename1.ui.plaf.UIManager;
import java.util.ArrayList;
import java.util.Enumeration;
import userclasses.StateMachine;

/**
 *
 * @author Martijn00
 */
public class Language {

    private static final String FALLBACK_LANGUAGE = "en";

    private static final Language INSTANCE = new Language();
    private ArrayList<String> supportedLanguages;

    public static Language getInstance() {
        return INSTANCE;
    }

    public void init()
    {
        supportedLanguages = new ArrayList<String>();

        Enumeration supportedByAPP = StateMachine.getResourceFile().listL10NLocales("Dictionary");
        while (supportedByAPP.hasMoreElements()) {
            supportedLanguages.add((String) supportedByAPP.nextElement());
        }

        setLanguage(getStartupLanguage());
    }

    private String getStartupLanguage()
    {
        String language = (String)Storage.getInstance().readObject("language");
        if (language == null) {
            language = FALLBACK_LANGUAGE;

            String systemLanguage = Display.getInstance().getLocalizationManager().getLanguage();
            if (isLanguageAvailable(systemLanguage)) {
                language = systemLanguage;
            }
        }

        return language;
    }

    /**
     * Set a new language. Please check first if this language is available!
     *
     * @param language
     */
    public void setLanguage(String language)
    {
        Storage.getInstance().writeObject("language", language);

        // Set language for UI
        UIManager.getInstance().setBundle(StateMachine.getResourceFile().getL10N("Dictionary", language));
    }

    public String getCurrentLanguage()
    {
        return (String) Storage.getInstance().readObject("language");
    }

    public boolean isLanguageAvailable(String language)
    {
        return supportedLanguages.contains(language);
    }

    public ArrayList<String> getSupportedLanguages()
    {
        return supportedLanguages;
    }
}
