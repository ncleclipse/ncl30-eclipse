/*******************************************************************************
 * This file is part of the NCL authoring environment - NCL Eclipse.
 *
 * Copyright (C) 2007-2012, LAWS/UFMA.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License version 2 for
 * more details. You should have received a copy of the GNU General Public 
 * License version 2 along with this program; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
 * 02110-1301, USA.
 *
 * For further information contact:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 ******************************************************************************/
package br.ufma.deinf.laws.ncleclipse.navigation;

import java.util.LinkedList;

public class NCLNavigationHistory {
	private static LinkedList<Position> positions = new LinkedList<Position>();
	private static int atual = -1;

	public static void movedcursor(String File, String position) {
		Position pos = new Position(File, position);

		if (!(positions.size() > 0
				&& positions.get(atual).getFile().equals(pos.getFile()) && positions
				.get(atual).getLine() == pos.getLine())) {

			if (atual < positions.size() - 1) {
				while (atual + 1 < positions.size())
					positions.removeLast();
			}

			else if (positions.size() >= 50) {
				positions.remove();
				atual--;
			}

			positions.add(pos);
			atual++;
		}
	}

	public static Position Prev() {
		if (atual < 1) {
			atual = 0;
			return null;
		}
		atual--;
		return positions.get(atual);
	}

	public static Position Next() {
		if (atual >= positions.size() - 1) {
			atual = positions.size() - 1;
			return null;
		}
		atual++;
		return positions.get(atual);
	}

	public static int size() {
		return positions.size();
	}

	public static void remove() {
		positions.removeLast();
		positions.removeLast();
		atual -= 2;
	}

}
