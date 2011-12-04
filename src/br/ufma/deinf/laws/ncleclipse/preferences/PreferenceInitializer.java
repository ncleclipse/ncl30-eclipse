/*******************************************************************************
 * Este arquivo é parte da implementação do ambiente de autoria em Nested 
 * Context Language - NCL Eclipse.
 * Direitos Autorais Reservados (c) 2007-2010 UFMA/LAWS (Laboratório de Sistemas 
 * Avançados da Web)
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob
 * os termos da Licença Pública Geral GNU versão 2 conforme publicada pela Free 
 * Software Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM 
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU
 * ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral do
 * GNU versão 2 para mais detalhes. Você deve ter recebido uma cópia da Licença
 * Pública Geral do GNU versão 2 junto com este programa; se não, escreva para a
 * Free Software Foundation, Inc., no endereço 59 Temple Street, Suite 330,
 * Boston, MA 02111-1307 USA.
 *
 * Para maiores informações:
 * - ncleclipse@laws.deinf.ufma.br
 * - http://www.laws.deinf.ufma.br/ncleclipse
 * - http://www.laws.deinf.ufma.br
 *
 *******************************************************************************
 * This file is part of the authoring environment in Nested Context Language -
 * NCL Eclipse.
 * Copyright: 2007-2010 UFMA/LAWS (Laboratory of Advanced Web Systems), All
 * Rights Reserved.
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
package br.ufma.deinf.laws.ncleclipse.preferences;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import br.ufma.deinf.laws.ncleclipse.NCLEditorPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = NCLEditorPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(PreferenceConstants.P_NCL_LAYOUT_EDITOR_ACTIVATE,
				false);
		store.setDefault(PreferenceConstants.P_SSH_RUN_IP, "192.168.64.129");
		store.setDefault(PreferenceConstants.P_SSH_RUN_USER, "root");
		store.setDefault(PreferenceConstants.P_SSH_RUN_PASSW, "telemidia");

		store.setDefault(PreferenceConstants.P_SSH_RUN_SCRIPT,
				"/misc/launcher.sh");
		store.setDefault(PreferenceConstants.P_SSH_RUN_WORKSPACE, "/misc/ncl30");

		store.setDefault(PreferenceConstants.P_LANGUAGE,
				"messagesPt.properties");

		store.setDefault(PreferenceConstants.P_PREVIEW, false);
		store.setDefault(PreferenceConstants.P_VALIDATION, true);
		store.setDefault(PreferenceConstants.P_POPUP_SUGESTION, false);
		store.setDefault(PreferenceConstants.P_LINK_AUTO_COMPLETE, false);
		store.setDefault(PreferenceConstants.P_SHOW_HELP_INFO_ON_AUTOCOMPLETE,
				false);
		
		store.setDefault(PreferenceConstants.P_ENABLE_REMOTE_SETTINGS, false);
		store.setDefault(PreferenceConstants.P_REMOTE_SETTINGS_PATH, "/usr/local/etc/ginga/files/contextmanager/contexts.ini");
	}

	private static String LINE_SEPARATOR = "@@@@";
	private static String FIELD_SEPARATOR = "####";

	public static String createList(String[][] commands) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < commands.length; i++) {
			if (i > 0) {
				stringBuilder.append(LINE_SEPARATOR);
			}
			String[] command = commands[i];
			for (int j = 0; j < command.length; j++) {
				if (j > 0) {
					stringBuilder.append(FIELD_SEPARATOR);
				}
				stringBuilder.append(command[j]);
			}
		}
		return stringBuilder.toString();
	}

	public static String[][] parseString(String commandsString) {
		if (commandsString != null && commandsString.length() > 0) {
			String[] commands = commandsString.split(Pattern
					.quote(LINE_SEPARATOR));
			String[][] parsedCommands = new String[commands.length][];
			for (int i = 0; i < commands.length; i++) {
				String command = commands[i];
				if (command.indexOf(FIELD_SEPARATOR) == -1) {
					parsedCommands[i] = new String[] { command, "*", command };
				} else {
					String[] fields = command.split(Pattern
							.quote(FIELD_SEPARATOR));
					parsedCommands[i] = new String[fields.length];
					for (int j = 0; j < fields.length; j++) {
						parsedCommands[i][j] = fields[j];
					}
				}
			}
			return parsedCommands;
		}
		return new String[0][0];
	}
}
