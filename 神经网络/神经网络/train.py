from sklearn.neural_network import MLPClassifier
from sklearn.model_selection import train_test_split
from sklearn.externals import joblib
import numpy as np
from read_data import read_data
file_name = 'data(1).txt'
# 读取数据
data = read_data(file_name)
# 转换数据格式
data = np.array(data)
# 取出训练数据的x
data_X = data[:, :-1]
# 取出训练数据的y
data_y = data[:, -1]
# 分割数据集为训练集与测试集
X_train, X_test, y_train, y_test = train_test_split(data_X, data_y, test_size=0.33, random_state=42)

# 下列参数代表， 三层隐藏层， 每层节点数为:8， 优化器为adam
clf = MLPClassifier(solver='adam', alpha=1e-10,hidden_layer_sizes=(8, 8, 8), max_iter=10000)
# 训练模型
clf.fit(X_train, y_train)
# 保存模型
joblib.dump(clf, 'model.m')
# 测试准确率
print("准确率:{}".format(clf.score(X_test, y_test)))