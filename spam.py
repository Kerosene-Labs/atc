import subprocess
import datetime

if __name__ == "__main__":
    for x in range(0, 100):
        process = subprocess.Popen(["curl", "-k", "-i", "-H", "'Host: api.weather.gov'", "https://localhost:8443"], stderr=subprocess.DEVNULL)
        