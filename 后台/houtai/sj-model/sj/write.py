from sklearn.externals import joblib
import numpy as np
import sys
import json

def xieru(data):
	file_handle = open('shuju.txt',mode='w')
	file_handle.write(data)


if __name__ == '__main__':
	data = sys.argv[1]
	print(data)
	xieru(data)



