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

package com.mapotempo.fleet.api.model.accessor;

import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusActionInterface;
import com.mapotempo.fleet.api.model.MissionStatusInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;

import java.util.List;

/**
 * The {@link MissionStatusTypeInterface} accessor.
 * An accessor provide actions on data as well as a notification mechanism.
 */
public interface MissionStatusAccessInterface extends AccessInterface<MissionStatusInterface> {

    /**
     * @return The specific {@link MissionStatusInterface}
     */
    @Override
    MissionStatusInterface get(String id);

    /**
     * {@inheritDoc}
     *
     * @return a new {@link MissionStatusInterface} unsaved
     */
    @Override
    MissionStatusInterface getNew();

    /**
     * @return The list of all {@link MissionStatusActionInterface}
     */
    @Override
    List<MissionStatusInterface> getAll();

    /**
     * {@inheritDoc}
     */
    @Override
    void addChangeListener(ChangeListener<MissionStatusInterface> changeListener);

    /**
     * {@inheritDoc}
     */
    @Override
    void removeChangeListener(ChangeListener<MissionStatusInterface> changeListener);

    List<MissionStatusInterface> getByMission(final MissionInterface mission);
}
