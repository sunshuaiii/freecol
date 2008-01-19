/**
 *  Copyright (C) 2002-2007  The FreeCol Team
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

package net.sf.freecol.common.model;

import java.lang.reflect.Method;

import net.sf.freecol.common.model.Unit.UnitState;
import net.sf.freecol.common.model.UnitType.DowngradeType;
import net.sf.freecol.util.test.FreeColTestCase;

public class DemotionTest extends FreeColTestCase {

    TileType plains = spec().getTileType("model.tile.plains");

    UnitType braveType = spec().getUnitType("model.unit.brave");
    UnitType colonistType = spec().getUnitType("model.unit.freeColonist");
    UnitType veteranType = spec().getUnitType("model.unit.veteranSoldier");
    UnitType artilleryType = spec().getUnitType("model.unit.artillery");
    UnitType damagedArtilleryType = spec().getUnitType("model.unit.damagedArtillery");

    EquipmentType muskets = spec().getEquipmentType("model.equipment.muskets");
    EquipmentType horses = spec().getEquipmentType("model.equipment.horses");

    public void testColonistDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit colonist = new Unit(game, tile1, dutch, colonistType, UnitState.ACTIVE);
        assertTrue(colonist.hasAbility("model.ability.canBeCaptured"));
        Unit soldier = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        assertTrue(soldier.hasAbility("model.ability.canBeCaptured"));
        soldier.equipWith(muskets, true);
        assertFalse(soldier.hasAbility("model.ability.canBeCaptured"));

        method.invoke(combatModel, colonist, soldier);
        assertEquals(colonistType, colonist.getType());
        assertEquals(french, colonist.getOwner());
        assertEquals(tile2, colonist.getTile());

    }

    public void testSoldierDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit soldier1 = new Unit(game, tile1, dutch, colonistType, UnitState.ACTIVE);
        soldier1.equipWith(muskets, true);
        Unit soldier2 = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        soldier2.equipWith(muskets, true);

        method.invoke(combatModel, soldier1, soldier2);
        assertEquals(colonistType, soldier1.getType());
        assertEquals(dutch, soldier1.getOwner());
        assertEquals(tile1, soldier1.getTile());
        assertTrue(soldier1.getEquipment().isEmpty());

        method.invoke(combatModel, soldier1, soldier2);
        assertEquals(colonistType, soldier1.getType());
        assertEquals(french, soldier1.getOwner());
        assertEquals(tile2, soldier1.getTile());
    }

    public void testDragoonDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit dragoon = new Unit(game, tile1, dutch, colonistType, UnitState.ACTIVE);
        dragoon.equipWith(muskets, true);
        dragoon.equipWith(horses, true);
        Unit soldier = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        soldier.equipWith(muskets, true);

        method.invoke(combatModel, dragoon, soldier);
        assertEquals(colonistType, dragoon.getType());
        assertEquals(dutch, dragoon.getOwner());
        assertEquals(tile1, dragoon.getTile());
        assertEquals(1, dragoon.getEquipment().size());
        assertEquals(muskets, dragoon.getEquipment().get(0));

        method.invoke(combatModel, dragoon, soldier);
        assertEquals(colonistType, dragoon.getType());
        assertEquals(dutch, dragoon.getOwner());
        assertEquals(tile1, dragoon.getTile());
        assertTrue(dragoon.getEquipment().isEmpty());

        method.invoke(combatModel, dragoon, soldier);
        assertEquals(colonistType, dragoon.getType());
        assertEquals(french, dragoon.getOwner());
        assertEquals(tile2, dragoon.getTile());
    }

    public void testDragoonDemotedByBrave() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        Player dutch = game.getPlayer("model.nation.dutch");
        Player inca = game.getPlayer("model.nation.inca");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(inca, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(inca, true);

        Unit dragoon = new Unit(game, tile1, dutch, colonistType, UnitState.ACTIVE);
        dragoon.equipWith(muskets, true);
        dragoon.equipWith(horses, true);
        Unit brave = new Unit(game, tile2, inca, braveType, UnitState.ACTIVE);

        method.invoke(combatModel, dragoon, brave);
        assertEquals(colonistType, dragoon.getType());
        assertEquals(dutch, dragoon.getOwner());
        assertEquals(tile1, dragoon.getTile());
        assertEquals(1, dragoon.getEquipment().size());
        assertEquals(muskets, dragoon.getEquipment().get(0));
        assertEquals(1, brave.getEquipment().size());
        assertEquals(horses, brave.getEquipment().get(0));

        method.invoke(combatModel, dragoon, brave);
        assertEquals(colonistType, dragoon.getType());
        assertEquals(dutch, dragoon.getOwner());
        assertEquals(tile1, dragoon.getTile());
        assertTrue(dragoon.getEquipment().isEmpty());
        assertEquals(2, brave.getEquipment().size());
        assertEquals(horses, brave.getEquipment().get(0));
        assertEquals(muskets, brave.getEquipment().get(1));

        method.invoke(combatModel, dragoon, brave);
        assertTrue(dragoon.isDisposed());

    }

    public void testScoutDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit scout = new Unit(game, tile1, dutch, colonistType, UnitState.ACTIVE);
        scout.equipWith(horses, true);
        Unit soldier = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        soldier.equipWith(muskets, true);

        method.invoke(combatModel, scout, soldier);
        scout.isDisposed();
    }

    public void testVeteranSoldierDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        assertEquals(colonistType, veteranType.getDowngrade(DowngradeType.CAPTURE));
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit soldier1 = new Unit(game, tile1, dutch, veteranType, UnitState.ACTIVE);
        soldier1.equipWith(muskets, true);
        Unit soldier2 = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        soldier2.equipWith(muskets, true);

        method.invoke(combatModel, soldier1, soldier2);
        assertEquals(veteranType, soldier1.getType());
        assertEquals(dutch, soldier1.getOwner());
        assertEquals(tile1, soldier1.getTile());
        assertTrue(soldier1.getEquipment().isEmpty());

        method.invoke(combatModel, soldier1, soldier2);
        assertEquals(colonistType, soldier1.getType());
        assertEquals(french, soldier1.getOwner());
        assertEquals(tile2, soldier1.getTile());
    }

    public void testArtilleryDemotedBySoldier() throws Exception {

        Game game = getStandardGame();
        CombatModel combatModel = game.getCombatModel();
        Method method = SimpleCombatModel.class.getDeclaredMethod("demote", Unit.class, Unit.class);
        method.setAccessible(true);
        assertEquals(damagedArtilleryType, artilleryType.getDowngrade(DowngradeType.DEMOTION));
        Player dutch = game.getPlayer("model.nation.dutch");
        Player french = game.getPlayer("model.nation.french");
        Map map = getTestMap(plains);
        game.setMap(map);
        Tile tile1 = map.getTile(5, 8);
        tile1.setExploredBy(dutch, true);
        tile1.setExploredBy(french, true);
        Tile tile2 = map.getTile(4, 8);
        tile2.setExploredBy(dutch, true);
        tile2.setExploredBy(french, true);

        Unit artillery = new Unit(game, tile1, dutch, artilleryType, UnitState.ACTIVE);
        Unit soldier = new Unit(game, tile2, french, colonistType, UnitState.ACTIVE);
        soldier.equipWith(muskets, true);

        method.invoke(combatModel, artillery, soldier);
        assertEquals(damagedArtilleryType, artillery.getType());
        assertEquals(dutch, artillery.getOwner());
        assertEquals(tile1, artillery.getTile());

        method.invoke(combatModel, artillery, soldier);
        assertTrue(artillery.isDisposed());
    }

}