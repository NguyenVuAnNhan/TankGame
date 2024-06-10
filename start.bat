@ECHO OFF
FOR /L %%i IN (1,1,100) DO (
    CSCRIPT //nologo build/multiple_popups.vbs
)
PAUSE