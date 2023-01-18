from urllib2 import urlopen
def get(url):
    return urlopen(url).read()
