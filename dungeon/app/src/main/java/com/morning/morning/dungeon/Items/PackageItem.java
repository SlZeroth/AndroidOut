package com.morning.morning.dungeon.Items;

import java.util.List;

/**
 * Created by juchan on 16. 3. 12..
 */
public class PackageItem {

    private List<InGame> inGames;

    public List<InGame> getInGames() {
        return inGames;
    }
    public void setInGames(List<InGame> inGames) {
        this.inGames = inGames;
    }

    public static class InGame {
        private String platformCode;
        private String pakeageName;
        private String appName;
        public String getPlatformCode() {
            return platformCode;
        }
        public void setPlatformCode(String platformCode) {
            this.platformCode = platformCode;
        }
        public String getPakeageName() {
            return pakeageName;
        }
        public void setPakeageName(String pakeageName) {
            this.pakeageName = pakeageName;
        }
        public String getAppName() {
            return appName;
        }
        public void setAppName(String appName) {
            this.appName = appName;
        }
    }
}
