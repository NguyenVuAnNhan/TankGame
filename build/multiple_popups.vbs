Set WshShell = CreateObject("WScript.Shell")

' Start a new process for each pop-up message
WshShell.Run "mshta.exe vbscript:CreateObject(""WScript.Shell"").Popup(""Ur Gay"",3,""Popup Title 1"")", 0, False
WshShell.Run "mshta.exe vbscript:CreateObject(""WScript.Shell"").Popup(""Ur Mom Gay"",3,""Popup Title 2"")", 0, False
WshShell.Run "mshta.exe vbscript:CreateObject(""WScript.Shell"").Popup(""Ur Dad Gay"",3,""Popup Title 3"")", 0, False