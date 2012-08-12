package com.icode.view.component;

import javax.swing.JLabel;

public class RichLabel extends JLabel
{
  public RichLabel(String paramString, int paramInt)
  {
    super("<html><div style=\"width: " + paramInt + "px;\">" + paramString + "</div></html>");
  }
}

/* Location:           C:\Users\Nes\Desktop\Thinet\flient.jar
 * Qualified Name:     thinlet.ui.RichLabel
 * JD-Core Version:    0.5.4
 */