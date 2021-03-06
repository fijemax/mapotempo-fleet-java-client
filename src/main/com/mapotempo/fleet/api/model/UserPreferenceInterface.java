/*
 * Copyright © Mapotempo, 2018
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.api.model;

/**
 * UserInterface.
 */
public interface UserPreferenceInterface extends MapotempoModelBaseInterface {

    enum Preference {
        MOBILE_DATA_USAGE("data_connection"),
        AUTOMATIC_DATA_UPDATE("automatic_data_update"),
        TRACKING_ENABLE("tracking_enable"),
        MAP_CURRENT_POSITION("map_current_position"),
        MAP_DISPLAY_ZOOM_BUTTON("map_display_zoom_button");

        private String preference = "";

        Preference(String name) {
            preference = name;
        }

        public String toString() {
            return preference;
        }
    }

    /**
     * Get the user login/name.
     *
     * @return A {@link String}
     */
    String getUser();

    /**
     * Get the user company.
     *
     * @return A {@link String}
     */
    String getCompanyId();

    /**
     * Get the user mobile data usage preference.
     *
     * @return A {@link String}
     */
    Boolean getBoolPreference(Preference preference);

    /**
     * Set the user mobile data usage preference.
     *
     * @param preference
     */
    void setBoolPreference(Preference preference, Boolean status);

    enum NightModePreference {
        AUTOMATIC("automatic"),
        NIGHT("night"),
        DAY("day");

        private String preference = "";

        NightModePreference(String name) {
            preference = name;
        }

        public String toString() {
            return preference;
        }

        static public NightModePreference fromString(String value) {
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("WARNING : fail string to enum conversion : " + e.getMessage());
                return AUTOMATIC;
            }
        }
    }

    /**
     * Get the user night mode preference.
     *
     * @return A {@link NightModePreference}
     */
    NightModePreference getNightModePreference();

    /**
     * Set the user night mode preference.
     *
     * @Boolean status
     */
    void setNightModePreference(NightModePreference status);

}
