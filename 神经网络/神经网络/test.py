from sklearn.externals import joblib
from read_data import read_data
import numpy as np


file_name = 'data(1).txt'
# 读取数据
data = read_data(file_name)

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
# 输出准确率
print("准确率:{}".format(clf.score(data_X, data_y)))
