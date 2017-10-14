#!/usr/bin/perl -w

# Instructions:
# [perl] checkcustom.pl SERVER
# valid servers are listed below, or printed when you run the script with no arguments
# requires the XML::Simple and Data::Dumper modules;  available from cpan.org


# TO DO:
# * legend for context.xml/resin.conf/jsps/sane chart
# * have script do *all* servers, & customize report


use strict;
use XML::Simple;
use Data::Dumper;

# hash of contexts that *might* be customized
# %possible_custom_contexts{context} = {context_xml => path, resin_conf => path\\filename, person_dir => path}
my %possible_custom_contexts;

my $server;			# this will hold the server name
my $contexts_dir;	# server's contexts directory (full UNC path)
my $resin_conf;		# server's resin.conf (full UNC path & filename)

# server definitions
# the resin_conf_structure element refers to data organization within the server's resin.conf -
#  it's a perl expression that's evaluated at compile-time, and must be configured for each server
#  (by uncommenting the "print Dumper $resin_conf;" line in parse_resin_conf() and inspecting output)
#  quotes around hyphenated hash keys are necessary to make use strict; happy
my %servers = (
	PSBETA => {
		contexts_path => '\\\\PSBETA\\PSDEV\\app\\review\\contexts',
		resin_conf => '\\\\PSBETA\\PSDEV\\resin2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"servlet-mapping"}'
	},
	PSSTAGE => {
		contexts_path => '\\\\PSSTAGE\\PSDEV\\ps3\\app\\ps3\\contexts',
		resin_conf => '\\\\PSSTAGE\\PSDEV\\ps3\\resin2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"/"}{"servlet-mapping"}'
	},
	PS3PUB => {
		contexts_path => '\\\\Ps3pub\\PS\\active\\ps\\contexts',
		resin_conf => '\\\\Ps3pub\\PS\\active\\resin2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"servlet-mapping"}'
	},
	PS3PAY => {
		contexts_path => '\\\\Ps3pay\\PS\\active\\ps\\contexts',
		resin_conf => '\\\\Ps3pay\\PS\\active\\resin2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"/"}{"servlet-mapping"}'
	},
	PS3META => {
		contexts_path => '\\\\ps3meta\\PS\\active\\ps\\contexts',
		resin_conf => '\\\\ps3meta\\PS\\active\\resin2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"servlet-mapping"}'
	},
	PSDEV => {
		contexts_path => '\\\\PSDEV\\ps\\contexts',
		resin_conf => '\\\\psdev\\resin-2.0.2\\conf\\resin.conf',
		resin_conf_structure => '$resin_conf->{"http-server"}{host}{"web-app"}{"/"}{"servlet-mapping"}'
	}
);

# get command-line argument(s), print usage information & die if there aren't any
if(scalar(@ARGV) lt 1) {
	printUsage();
} else {
	if(!$servers{uc($ARGV[0])}) {
		die "\n\"$ARGV[0]\" is not a supported server!\n";
	} else {
		$server = uc($ARGV[0]);
		$contexts_dir = $servers{$server}{contexts_path};
		$resin_conf = $servers{$server}{resin_conf};
	}
}

print "\n";
walk_contexts_path($contexts_dir);
parse_resin_conf($resin_conf);
printReport();

# prints usage information and exits
sub printUsage {
	print "\nUsage:  perl checkcustom.pl SERVER\n\n";
	print "Valid servers:\n";
	foreach my $key (sort keys %servers) {
		print "  $key\n";
	}
	print "\n";
	exit;
}

# reports on customized contexts' sanity
sub printReport {
	my $context;
	my $context_xml;
	my $custom_jsp;
	my $resin_conf;
	my $sane;
	my $checked = "*";
	my $unchecked = " ";
	
	my @sane_contexts;
	my @insane_contexts;

	print "\nCustomized Context Sanity Report for $server\n\n";
	print "context                         context.xml  custom JSPs  resin.conf   sane\n";
	print "---------------------------------------------------------------------------\n";

format report =
@<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  @||||||||||| @|||||||||| @||||||||||    @<<<<<<<
$context, $context_xml, $custom_jsp, $resin_conf, $sane;
.

	local $~ = "report";
	foreach my $key (sort keys %possible_custom_contexts) {
		$context = $key;
		$sane = 0;
		if($possible_custom_contexts{$key}->{context_xml}) {
			$context_xml = $checked;
			$sane++;
		} else {
			$context_xml = $unchecked;
		}
		if($possible_custom_contexts{$key}->{person_dir}) {
			$custom_jsp = $checked;
			$sane++;
		} else {
			$custom_jsp = $unchecked;
		}
		if($possible_custom_contexts{$key}->{resin_conf}) {
			$resin_conf = $checked;
			$sane++;
		} else {
			$resin_conf = $unchecked;
		}
		if($sane == 3) {
			$sane = "Yes";
			push @sane_contexts, $context;
		} else {
			$sane = "No";
			push @insane_contexts, $context;
		}

		write;
	}
	
	@sane_contexts = sort @sane_contexts;
	print "\nSane Contexts:\n";
	while(@sane_contexts) {
		my $context = pop @sane_contexts;
		print "   $context (at $possible_custom_contexts{$context}->{context_xml})\n";
	}
	
	@insane_contexts = sort @insane_contexts;
	print "\nNot Sane Contexts:\n";
	while(@insane_contexts) {
		my $context = pop @insane_contexts;
		my $path;
		if($possible_custom_contexts{$context}->{context_xml}) {
			print "   $context (at $possible_custom_contexts{$context}->{context_xml})\n";
		} elsif ($possible_custom_contexts{$context}->{person_dir}) {
			print "   $context (at $possible_custom_contexts{$context}->{person_dir})\n";
		} else {
			print "   $context (no equivalent found in contexts path)\n";
		}
	}
	
}

# parse resin.conf XML & try to find customized contexts based on servlet-mapping fields
sub parse_resin_conf {
	my $resin_conf_path = shift;
	my @contexts;
	
	# get servlet mappings from resin.conf, get their url-mapping properties, remove everything but a possible context name, and throw them in @resin_conf_custom_contexts
	my $resin_conf = XMLin($resin_conf_path) or die "parse_resin_conf() - Couldn't open $resin_conf_path - $!\n";
	# uncomment the next line to get a perl-ish output of resin.conf's structure (for configuring a new %servers entry)
	#print Dumper $resin_conf;
	while(  @{eval $servers{$server}{resin_conf_structure} }) {
		my $servlet_mapping = shift @{eval $servers{$server}{resin_conf_structure}};
		if( %{ $servlet_mapping }->{'url-pattern'} ) {
			if ( %{ $servlet_mapping }->{'url-pattern'} =~ m/\/(\w+)\//) {
				push @contexts, $1;
			}
		}
	}

	# glean custom contexts from the stripped servlet mappings
	# (by finding url patterns with >=3 mappings)
	my %context_counts;
	while(@contexts) {
		my $temp = pop @contexts;
		unless($context_counts{$temp}) {
			$context_counts{$temp} = 1;
		} else {
			$context_counts{$temp}++;
		}
	}
	# add discovered custom contexts to our hash of possible custom contexts
	foreach my $key (keys %context_counts) {
	    if($context_counts{$key} >= 3) {
	        if($possible_custom_contexts{$key}) {
	        	$possible_custom_contexts{$key}->{resin_conf} = "$key";
	        } else {
	        	$possible_custom_contexts{$key} = {resin_conf => $key};
	        }
	    }
	}
}

# walks a /contexts tree and tests each context.xml to see if it's customized
# (by inspecting the proxy element of context.xml)
# also checks for the presence of a 'person' subdir in the context dir
sub walk_contexts_path {
	my $contexts_path = shift;
	my %system_contexts = ("administration" => 1, "jsp_holder" => 1, "WEB-INF" => 1);
	
	opendir(CONTEXTS_DIR, $contexts_path) or die "walk_contexts_path() - Couldn't open $contexts_path - $!\n";
	my @contexts = grep { $_ ne '.' and $_ ne '..'} readdir(CONTEXTS_DIR);
	foreach my $dir (sort @contexts) {
		if(-d "$contexts_path\\$dir" and not $system_contexts{$dir}) {
			parse_context_xml("$contexts_path\\$dir\\context.xml");
			if(-d "$contexts_path\\$dir\\person") {
				if($possible_custom_contexts{$dir}) {
					$possible_custom_contexts{$dir}->{person_dir} = "$contexts_path\\$dir";
				} else {
					$possible_custom_contexts{$dir} = {person_dir => "$contexts_path\\$dir"};
				}
			}
		}
	}
}

# takes a context.xml file and determines whether it belongs to a customized context
# (by inspecting the "proxy" element)
sub parse_context_xml {
	my $context_xml_path = shift;
	
	if(-e $context_xml_path) {
		my $context_xml = XMLin($context_xml_path) or die "parse_context_xml() - Couldn't open/process $context_xml_path - $!\n";		# parse context.xml (i love you, perl)
		if($context_xml->{proxy} ne 'jsp_holder') {	# if the proxy element != 'jsp_holder', add the path & context name to our list
			$context_xml_path =~ s/\\context.xml//;
			if($possible_custom_contexts{$context_xml->{proxy}}) {
				$possible_custom_contexts{$context_xml->{proxy}}->{context_xml} = $context_xml_path;
			} else {
				$possible_custom_contexts{$context_xml->{proxy}} = {context_xml => $context_xml_path};
			}
		}
	}
}