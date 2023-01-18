import sys
import subprocess
import os
import time

class bcolors:
    HEADER = '\033[95m'
    OKGREEN = '\033[92m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'


def header(text): return bcolors.HEADER + bcolors.BOLD + text + bcolors.ENDC
def error(text):  return bcolors.FAIL + text + bcolors.ENDC
def ok(text):  return bcolors.OKGREEN + text + bcolors.ENDC
