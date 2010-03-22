#!/bin/bash
#This script is intended to update the ncl-eclipse-update-site
PROGRAM_NAME='addhead.sh'
PROGRAM_VERSION='$Id: build.sh 0.1 2010/03/21 robertogerson'
AUTHORS='Roberto Azevedo'
BUGS_TO='robertogerson@telemidia.puc-rio.br'


#get the atual path
cwd=`pwd`

#arguments defaults
helpflag='false'
showfiles='false'
ROOT='../'
PATTERN='*.java'

#will be used by the filter_file
FILES_TO_COPY=''

#Parse the arguments and set the variables to be handled by the script
parse_args() {
	echo "Running with parameters: '$*'"
	until [ -z "$1" ]
	do
		case "$1" in
			(-h|--help) helpflag='true';;
			(--show-files) showfiles='true';;
			(--root) ROOT="$2"; shift;;
			(-p|--pattern) PATTERN="$2"; shift;;
			(--) shift break;;
		esac
		shift
	done
}

print_help() {
	echo "\
Usage: $PROGRAM_NAME [OPTIONS...] --root ROOT_PATH
Addhead is a script to add heading to files...

Options:
	-h,--help		print this help

	--show-files		show files to be changed. None change is
				realized.

	--root			the root path to files where the head should be
				putted. The files will be searched in all subdirectories
				with the command 'find'.

	-p,--pattern		pattern to find files. Default is *.java

	-v,--verbose		verbose mode (not working yet)
	
Report bugs to: <$BUGS_TO>."
}

do_actions() {
	if test "$helpflag" = 'true'; then
		print_help
		return 1
	fi


	files=`find "$ROOT" -name "$PATTERN"`

	if test "$showfiles" = 'true'; then
		echo $files
		return;
	fi

	for i in $files 
	do
		echo "Adding Head to file: $i"
		awk ' BEGIN { RS="" }
		      FILENAME==ARGV[1] { s=$0 }
		      FILENAME==ARGV[2] { r=$0 }
		      FILENAME==ARGV[3] { sub(s,r) ; print ; if (NF != 0) print ""}
		    ' addhead.regex addhead.content $i > $i.tmp
		    #remove the last line
		
		sed '$d' $i.tmp > $i
		rm $i.tmp
	done
}

parse_args "$@"
do_actions

cd $cwd
