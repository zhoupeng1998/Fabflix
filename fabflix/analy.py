import sys

def analyze () :
    allLines = list()
    for i in range(1,len(sys.argv)):
        with open(sys.argv[i], 'r', encoding='utf-8') as iFile :
            lines = iFile.readlines()
            for l in lines:
                allLines.append(l)
            iFile.close()
    ts = 0.0
    tj = 0.0
    for r in allLines:
        rec = r.strip().split(',')
        ts += int(rec[0])
        tj += int(rec[1])
    ts /= len(allLines)
    tj /= len(allLines)
    ts /= 1000000
    tj /= 1000000
    print("TS: ", ts)
    print("TJ: ", tj)

if __name__ == "__main__":
    analyze()