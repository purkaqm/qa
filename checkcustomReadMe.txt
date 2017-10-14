checkcustom.pl - A Perl script for verifying the sanity of all customized contexts on a PS3 server

* Usage:  perl checkcustom.pl SERVER
* individual servers must be configured in the script.  The script currently supports the following servers:
	PS3META
	PS3PAY
	PS3PUB
	PSBETA
	PSDEV
	PSSTAGE
* requires Perl.  A Win32 Perl distribution is available from activestate.com (for which the script is written)
* requires the following Perl modules, available from cpan.org:
	XML::Simple
	Data::Dumper
