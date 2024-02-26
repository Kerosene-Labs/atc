import subprocess
import datetime

if __name__ == "__main__":
    for x in range(0, 10000):
        process = subprocess.Popen(["curl", "-k", "-i", "-H", "'Host: google.com'", "https://localhost:8443"], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        