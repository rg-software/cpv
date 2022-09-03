package cpv;

import java.awt.*;
import com.formdev.flatlaf.FlatLightLaf;
import cpv.Runner.*;

/* Concurrent Programs Verifier

   Copyright 2003 by Maxim Mozgovoy and Mordechai (Moti) Ben-Ari.
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE
   See the GNU General Public License for more details

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
   02111-1307, USA.
*/

public class Application    // main class
{
    public static MainFrame frame;                     // main application frame
    public static Program theprogram = null;           // currently loaded program
    public static ILRunner therunner = new ILRunner(); // ILRunner instance
    public static String CurrentFileName = null;       // filename of the currently loaded program

    public Application()
    {
    	FlatLightLaf.setup();
        frame = new MainFrame();
        frame.validate();  // validate frame size
        
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frameSize.height = Math.min(frameSize.height, screenSize.height);
        frameSize.width = Math.min(frameSize.width, screenSize.width);

        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        new Application();
    }
}