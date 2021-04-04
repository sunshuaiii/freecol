/**
 *  Copyright (C) 2002-2021   The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.server.model;

import java.util.Random;

import net.sf.freecol.common.util.LogBuilder;
import net.sf.freecol.common.networking.ChangeSet;


/**
 * Interface for server-side objects which need to update every turn.
 */
public interface TurnTaker {

    /**
     * Executes new-turn actions for this server object.
     *
     * @param random A pseudo-random number source.
     * @param lb A {@code LogBuilder} to log to.
     * @param cs A {@code ChangeSet} to update.
     */
    public void csNewTurn(Random random, LogBuilder lb, ChangeSet cs);
} 
