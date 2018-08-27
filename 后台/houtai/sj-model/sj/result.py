from sklearn.externals import joblib
from read_data import read_data
import numpy as np
import sys
import json


class qwe():
	def model():
		data = read_data()
		# 转换数据格式
		data = np.array(data)
		# 取出训练数据的x
		data_X = data[:, :-1]
		# 取出训练数据的y
		data_y = data[:, -1]
		# 载入模型
		clf = joblib.load('model.m')
		# 预测模型的分类
		pred = clf.predict(data_X)
		return pred
		


	def reqwe(pred):
		# 健康
		if pred == [0.]:
			result = 1
		# 亚健康
		if pred == [1.]:
			result = 2
		# 不健康
		if pred == [2.]:
			result = 3

		print(result)

	def qwe1():
		pred = qwe.model()
		qwe.reqwe(pred)

if __name__ == '__main__':
	qwe.qwe1()
