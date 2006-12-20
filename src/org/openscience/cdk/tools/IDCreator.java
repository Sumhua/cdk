/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2006  The Chemistry Development Kit (CDK) Project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package org.openscience.cdk.tools;

import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomContainerSetManipulator;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;

import java.util.Iterator;
import java.util.List;

/**
 * Class that provides methods to give unique IDs to ChemObjects.
 * Methods are implemented for Atom, Bond, AtomContainer, AtomContainerSet
 * and Reaction. It will only create missing IDs. If you want to create new
 * IDs for all ChemObjects, you need to delete them first.
 *
 * @cdk.module standard
 *
 * @author   Egon Willighagen
 * @cdk.created  2003-04-01
 *
 * @cdk.keyword  id, creation
 * @cdk.bug      1455341
 */
public class IDCreator {

    /**
     * A list of taken IDs.
     */
    private List tabuList;
    
    /**
     * Keep track of numbers.
     */
    int atomCount;
    int bondCount;
    int moleculeCount;
    int reactionCount;
    
    public IDCreator() {
        reset();
    }
    
    public void reset() {
        tabuList = null;
        atomCount = 0;
        bondCount = 0;
        moleculeCount = 0;
        reactionCount = 0;
    }
    
    /**
     * Labels the Atom's and Bond's in the AtomContainer using the a1, a2, b1, b2
     * scheme often used in CML.
     *
     * @see #createIDs(IAtomContainerSet)
     */
    public void createIDs(IAtomContainer container) {
        if (tabuList == null) tabuList = AtomContainerManipulator.getAllIDs(container);
        
        if (container.getID() == null) {
            moleculeCount++;
            while (tabuList.contains("m" + moleculeCount)) moleculeCount++;
            container.setID("m" + moleculeCount);
        }
        
        java.util.Iterator atoms = container.atoms();
        while(atoms.hasNext()) {
        	IAtom atom = (IAtom)atoms.next();
            if (atom.getID() == null) {
                atomCount++;
                while (tabuList.contains("a" + atomCount)) atomCount++;
                atom.setID("a" + atomCount);
            }
        }

        Iterator bonds = container.bonds();
        while (bonds.hasNext()) {
            IBond bond = (IBond) bonds.next();
            if (bond.getID() == null) {
                bondCount++;
                while (tabuList.contains("b" + bondCount)) bondCount++;
                bond.setID("b" + bondCount);
            }
        }
    }

    public void createIDs(IMoleculeSet containerSet) {
    	createIDs((IAtomContainerSet)containerSet);
    }    
    
    /**
     * Labels the Atom's and Bond's in each AtomContainer using the a1, a2, b1, b2
     * scheme often used in CML. It will also set id's for all AtomContainers, naming
     * them m1, m2, etc.
     * It will not the AtomContainerSet itself.
     */
    public void createIDs(IAtomContainerSet containerSet) {
        if (tabuList == null) tabuList = AtomContainerSetManipulator.getAllIDs(containerSet);

        if (containerSet.getID() == null) {
            moleculeCount++;
            while (tabuList.contains("molSet" + moleculeCount)) moleculeCount++;
            containerSet.setID("molSet" + moleculeCount);
        }

        java.util.Iterator acs = containerSet.atomContainers();
        while (acs.hasNext()) {
        	IAtomContainer container = (IAtomContainer)acs.next();
            if (container.getID() == null) {
                createIDs(container);
            }
        }
    }
    
    /**
     * Labels the reactants and products in the Reaction m1, m2, etc, and the atoms
     * accordingly, when no ID is given.
     */
    public void createIDs(IReaction reaction) {
        if (tabuList == null) tabuList = ReactionManipulator.getAllIDs(reaction);
        
        if (reaction.getID() == null) {
            reactionCount++;
            while (tabuList.contains("r" + reactionCount)) reactionCount++;
            reaction.setID("r" + reactionCount);
        }

        java.util.Iterator reactants = reaction.getReactants().atomContainers();
        while (reactants.hasNext()) {
            createIDs((IAtomContainer)reactants.next());
        }
        java.util.Iterator products = reaction.getProducts().atomContainers();
        while (products.hasNext()) {
            createIDs((IAtomContainer)products.next());
        }
    }
    
    public void createIDs(IReactionSet reactionSet) {
        for (java.util.Iterator iter = reactionSet.reactions(); iter.hasNext();) {
            createIDs((IReaction)iter.next());
        }
    }
    
    public void createIDs(IChemFile file) {
    	java.util.Iterator sequences = file.chemSequences();
    	while (sequences.hasNext()) {
    		createIDs((IChemSequence)sequences.next());
    	}
    }
    
    public void createIDs(IChemSequence sequence) {
    	java.util.Iterator models = sequence.chemModels();
    	while (models.hasNext()) {
    		createIDs((IChemModel)models.next());
    	}
    }
    
    public void createIDs(IChemModel model) {
    	ICrystal crystal = model.getCrystal();
    	if (crystal != null) createIDs(crystal);
    	IMoleculeSet moleculeSet = model.getMoleculeSet();
    	if (moleculeSet != null) createIDs(moleculeSet);
    	IReactionSet reactionSet = model.getReactionSet();
    	if (reactionSet != null) createIDs(reactionSet);
    }
    
    public void createIDs(ICrystal crystal) {
    	createIDs((IAtomContainer)crystal);
    }
}
