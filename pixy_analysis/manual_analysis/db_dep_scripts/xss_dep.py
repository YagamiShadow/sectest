import csv

string_types = ["CHAR", "VARCHAR", "BINARY", "VARBINARY", "BLOB", "TEXT", "ENUM", "SET"]


def read_csv(path, read_fn, columns=3):
    with open(path) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        title = None
        res = {}
        for row in csv_reader:
            if len(row) < columns:
                continue
            if title is None:
                title = row
                continue
            read_fn(row, res)
        return res


def read_db_dep(path):
    def f(row, res):
        row[2] = [x for x in [x.strip() for x in row[2].split("|")] if x != ""]
        res[row[0]] = row

    return read_csv(path, f)


def read_vuln_dep(path):
    def f(row, res):
        row[2] = [x for x in [x.strip() for x in row[2].split("|")] if x != ""]
        res[row[0]] = row

    return read_csv(path, f)


def is_db_entry_vulnerable(entry):
    typ = entry[1].upper()
    if typ not in string_types:
        return entry[0], None
    return entry[0], entry[2]


def is_vuln_entry_vulnerability(entry, db_entries):
    vulnerable = []
    v = [is_db_entry_vulnerable(db_entries[e]) for e in entry[2]]
    for p in v:
        vulnerable.append((entry[0], p[0], p[1]))
    return vulnerable


chars = "abcdefghijklmnopqrstuvwxyz"
db_deps = read_db_dep("xss_db_dep.csv")
vn_deps = read_vuln_dep("xss_vuln_dep.csv")
result = {r: is_vuln_entry_vulnerability(vn_deps[r], db_deps) for r in vn_deps}
tp = []
fp = []
tpd = {}
fpd = {}
print("ALL VULNERABILITIES VARIANTS:")
for r in result:
    entry = result[r]
    i = 0
    for se in entry:
        a, b, c = se
        v = "TP"
        if c is None:
            v = "FP"
            c = "Column datatype cannot contain XSS payload"
        elif len(c) == 0:
            v = "FP"
            c = "The data inserted in the database is always sanitized"
        else:
            c = "See test " + a + ".java"

        r = str(v == "TP") + "," + a + str(chars[i]).upper() + "," + b + "," + c
        print(r)
        if v == "TP":
            tp.append(r)
            tpd[a] = r
        else:
            fp.append(r)
            fpd[a] = r
        i += 1
print()
print("TRUE POSITIVE VULNERABILITIES: %d (%d files)" % (len(tp), len(tpd)))
for t in tp:
    print(t)
print()
print("FALSE POSITIVE VULNERABILITIES: %d (%d files)" % (len(fp), len(fpd)))
for t in fp:
    print(t)
