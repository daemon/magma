package net.rocketeer.magma.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class FormatMessageStore
{
  private final Map<String, String> _messageTable = new HashMap<>();

  public FormatMessageStore(FileConfiguration config)
  {
    ConfigurationSection section = config.getConfigurationSection("messages");
    for (String key : section.getKeys(false))
      this._messageTable.put(key, section.getString(key));
  }

  public FormatMessage lookup(String messageName)
  {
    return new FormatMessage(this._messageTable.get(messageName));
  }

  public static class FormatMessage
  {
    private final String _fmtString;

    private FormatMessage(String fmtString)
    {
      this._fmtString = fmtString;
    }

    public FormatMessage format(String paramName, String text)
    {
      return new FormatMessage(this._fmtString.replaceAll("\\$\\{" + paramName + "\\}", text));
    }

    public FormatMessage rawFormat(Object... args)
    {
      return new FormatMessage(String.format(this._fmtString, args));
    }

    public FormatMessage colorFormat()
    {
      StringBuilder builder = new StringBuilder();
      int i = 0;
      boolean expectColorCode = false;
      while (i < this._fmtString.length())
      {
        char c = this._fmtString.charAt(i);
        builder.append(c);
        if (c == '&')
        {
          expectColorCode = true;
          ++i;
          continue;
        } else if (!expectColorCode) {
          ++i;
          continue;
        }

        expectColorCode = false;
        ++i;
        if (!((c <= 'f' && c >= 'a') || (c <= '9' && c >= '0')))
          continue;
        builder.deleteCharAt(builder.length() - 1);
        builder.append(ChatColor.getByChar(c));
        builder.deleteCharAt(builder.length() - 3);
      }

      return new FormatMessage(builder.toString());
    }

    public String toString()
    {
      return this._fmtString;
    }
  }
}
