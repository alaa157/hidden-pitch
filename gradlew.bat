@echo off
setlocal
set "GRADLE_VERSION=9.6.0"
set "GRADLE_CACHE=%USERPROFILE%\.gradle\manual-wrapper"
set "GRADLE_HOME_DIR=%GRADLE_CACHE%\gradle-%GRADLE_VERSION%"
if not exist "%GRADLE_HOME_DIR%\bin\gradle.bat" (
  if not exist "%GRADLE_CACHE%" mkdir "%GRADLE_CACHE%"
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; $zip='%GRADLE_CACHE%\gradle-%GRADLE_VERSION%-bin.zip'; Invoke-WebRequest -Uri 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath '%GRADLE_CACHE%' -Force; Remove-Item $zip"
  if errorlevel 1 exit /b %ERRORLEVEL%
)
call "%GRADLE_HOME_DIR%\bin\gradle.bat" -p "%~dp0" %*
exit /b %ERRORLEVEL%
