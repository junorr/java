#!/bin/bash

echo "--------------------------------"
echo "    Syscheck Install Script"
echo "       Copyright (c) 2013"
echo " Juno Roesler - juno@pserver.us"
echo -e "--------------------------------\n"

if [ "$1" = "-h" -o "$1" = "--help" ]; then
  echo "install.sh [option] <install_dir>"
  echo "  [sudo] <pwd>  : Executes installation as sudo (sudo's pasword needed);"
  echo "  [-f/--force]  : Force install even if <install_dir> already exists;"
  echo "  [-h/--help]   : Show this help text;"
  exit 0
fi


curr_dir=$(readlink -f $0)
curr_dir=$(dirname $curr_dir)

install_dir=$1
force="n"


if [ "$1" = "sudo" ]; then
  if [ "$3" = "-f" -o "$3" = "--force" ]; then
    install_dir=$4
    force="y"
    echo "* [WARNING] Forced installation Activated!"
  else
    install_dir=$3
  fi
elif [ "$1" = "-f" -o "$1" = "--force" ]; then
  force="y"
  install_dir=$2
  echo "* [WARNING] Forced installation Activated!"
fi


if [ -z "$install_dir" ]; then
  echo "# Invalid install directory!"
  exit 1
fi


if [ -e "$install_dir" -a "$force" = "n" ]; then
  echo "* Directory for installation already exists!"
  echo "  [$install_dir]"
  echo "* Use '-f/--force' option if you want to force installation"
  echo -e "# Aborting...\n"
  exit 55
fi


tarfile="$curr_dir"/syscheck.tar.gz
res_rm=""
res_mkdir=""
res_tar=""
res_chmod=""
res_serv=""
res_jar=""

if [ "$1" = "sudo" ]; then
  if [ -e "$install_dir" ]; then
    echo -e "* [WARNING] All content of [$install_dir] will be deleted!\n"
    res_rm=$(echo -e "$2\n" | sudo -S -E -p "" rm -rf "$install_dir" 2>&1)
  fi
  res_mkdir=$(echo -e "$2\n" | sudo -S -E -p "" mkdir -p "$install_dir" 2>&1)
  res_tar=$(echo -e "$2\n" | sudo -S -E -p "" tar xzf "$tarfile" --directory="$install_dir" --overwrite --no-same-owner --no-same-permissions 2>&1)
  res_chmod=$(echo -e "$2\n" | sudo -S -E -p "" chmod 766 $install_dir/* 2>&1)
  res_serv=$(echo -e "$2\n" | sudo -S -E -p "" chmod 755 $install_dir/service.sh 2>&1)
  res_jar=$(echo -e "$2\n" | sudo -S -E -p "" chmod 755 $install_dir/syscheck.jar 2>&1)
  res_jar=$(echo -e "$2\n" | sudo -S -E -p "" chmod -R 755 $install_dir/lib 2>&1)
else
  if [ -e "$install_dir" ]; then
    echo -e "* [WARNING] All content of [$install_dir] will be deleted!\n"
    res_rm=$(rm -rf "$install_dir" 2>&1)
  fi
  res_mkdir=$(mkdir -p "$install_dir" 2>&1)
  res_tar=$(tar xzf  "$tarfile" --directory="$install_dir" --overwrite --no-same-owner --no-same-permissions 2>&1)
  res_chmod=$(chmod 766 $install_dir/* 2>&1)
  res_serv=$(chmod 755 $install_dir/service.sh 2>&1)
  res_jar=$(chmod 755 $install_dir/syscheck.jar 2>&1)
  res_jar=$(chmod -R 755 $install_dir/lib 2>&1)
fi


error="n"
if [ ! -z "$res_rm" ]; then
  echo "# Error removing [$install_dir]:"
  echo -e "$res_rm\n"
  error="y"
fi
if [ ! -z "$res_mkdir" ]; then
  echo "# Error making directory [$intall_dir]:"
  echo -e "$res_mkdir\n"
  error="y"
fi
if [ ! -z "$res_tar" ]; then
  echo "# Error on untar:"
  echo -e "$res_tar\n"
  error="y"
fi
if [ ! -z "$res_chmod" ]; then
  echo "# Error changing permissions:"
  echo -e "$res_chmod\n"
  error="y"
fi
if [ ! -z "$res_serv" ]; then
  echo "# Error changing service permissions:"
  echo -e "$res_serv\n"
  error="y"
fi
if [ ! -z "$res_jar" ]; then
  echo "# Error changing jar permissions:"
  echo -e "$res_jar\n"
  error="y"
fi

if [ "$error" = "y" ]; then
  echo -e "(try to run: install.sh sudo <pwd> -f <dir> )\n"
  echo -e "# Installation failed!\n"
else
  echo "* Syscheck module installed in [$install_dir]"
  echo -e "* Installation Successful!\n"
fi