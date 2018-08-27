
def read_data():
    data = []
    # 打开文件
    with open('./shuju.txt', 'r', encoding='utf8') as f:
        # 遍历每一行
        for line in f.readlines():
            # 删除前后空格， 并且分割一行中的数据
            line_txt = line.strip().split('  ')
            if len(line_txt) == 16:
                dst_line_txt = []
                # 遍历一行中的所有元素
                for iterm in line_txt:
                    if iterm == '\u7537' or iterm == '\u5973':
                        # 男女转换成数字
                        if iterm == '\u7537':
                            dst_line_txt.append(0)
                        else:
                            dst_line_txt.append(1)
                    else:
                        # 把文本数字转为浮点型
                        dst_line_txt.append(float(iterm))
                # 把最终的数据保存起来
                data.append(dst_line_txt)
    return data