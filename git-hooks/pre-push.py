#!/usr/bin/python
# -*- coding: UTF-8 -*-
from util import *


def start(environment = "prod"):
    with open(os.devnull, 'w') as shutup:
        subprocess.call("cd ../examples/ && sh ./" + environment + ".sh", shell=True, stdout=shutup, stderr=shutup)
    time.sleep(60)
def end():
    os.system("docker kill $(docker ps -q)")


def verboseRun(environment, test):
    print header(environment + " - " + test.__name__),
    testResult = test()
    if testResult:
          print ok('✔')
    else: print fail('✘')
    return testResult

print ok("Executing pre-push hook. This will take 2 minutes.")

results = []
for e in environments:
    start()
    for t in tests:
        result = verboseRun(e,t)
        results.append(result)
    end()

if all(results):
      print ok("All tests passed. 💚")
else: print error("Not all tests passed. 💔")
sys.exit(not all(results))
