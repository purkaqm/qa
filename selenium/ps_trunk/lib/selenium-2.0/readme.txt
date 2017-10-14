now we use:
 * selenium-server-standalone-2.3.0 and chromedriver_win32_14.0.836.0 for googlechrome (currently version 14 and 16)
 * selenium-server-standalone-2.0b3 for ie* and ff* (web-driver way, dnd is broken in latest versions)
 * selenium-server-standalone-2.8.0 for ie* (rc-driver way, command type is broken in latest versions)
 * selenium-server-standalone-2.18.0 (main version) for other browsers (ie*,ff*) (rc-driver way)
for more details see com.powersteeringsoftware.libs.core.SeleniumDriverFactory.Version class
to build with specified server use command "ant -Dselenium.version={version}"(see build.xml), where version equals 2.3.0, 2.0b3, 2.18.0, etc.
