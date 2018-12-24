import subprocess
import json
import itertools
import logging

#You will need universal ctags in your PATH
def getFunctions(filestring, logging, file_path):
    func_lines = []
    func_bodies = []
    file_lines = filestring.splitlines()
    try:
        ctags_output = subprocess.check_output(["ctags", "--fields=+ne", "--c-types=f", "--output-format=json", "--sort=no", file_path])
    except Exception as e:
        logging.warning("File " + file_path + " cannot be parsed. (1)" + str(e))
        #logging.warning('Traceback:' + traceback.print_exc())
        return (None, None, [])
    for input in ctags_output.splitlines():
        data = json.loads(input)
        kind = data["kind"]
        if kind != "function":
            continue
        start = int(data["line"])
        end = int(data["end"])
        body = []
        for line in itertools.islice(file_lines , start - 1, end):
            body.append(line)
        body = '\n'.join(body)
        func_lines.append((start,end))
        func_bodies.append(body)
    if (len(func_lines) != len(func_bodies)):
        logging.warning("File " + file_path + " cannot be parsed. (3)")
        return (None,None)
    else:
        logging.warning("File " + file_path + " successfully parsed.")
        return(func_lines,func_bodies)
'''
path="null_pointer.c"
fileopen = open(path)
file = fileopen.read()
print(getFunctions(file, logging, path))
'''
