from api import *
from docker import *
from console import *


def health():
    result = urlopen("http://0.0.0.0:8081/health").read()
    return 'service is healthy' in result
def api():
    result = urlopen("http://0.0.0.0:8081/state/SujetoActor/1").read()
    return '"saldo" : -244.72' in result
def database():
    return cassandra()

tests = [
    health,
    api,
    database,
    # 'other test!'
]
environments = [
    "dev",
    "prod",
    # 'other environment!'
]
