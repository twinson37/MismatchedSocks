import torch
import os
import sys

arg = sys.argv
home_path = os.path.expanduser('~')
# model = torch.hub.load('ultralytics/yolov5', 'custom', path='/Users/kimjungi/back/eunhy/yolov5/best.pt')
model = torch.hub.load(f'{home_path}/yolov5-master', 'custom', path= f'{home_path}/yolov5/best.pt', source='local')  

# Images
im = '/Users/kimjungi/KakaoTalk_Photo_2023-01-31-11-04-51 021.jpeg'  # or file, Path, URL, PIL, OpenCV, numpy, list
# im = f'{home_path}/img/{arg}'

# Inference
results = model(im)

# Results
results.save()  # or .show(), .save(), .crop(), .pandas(), etc.

results.xyxy[0]  # im predictions (tensor)
results.pandas().xyxy[0]  # im predictions (pandas)
#      xmin    ymin    xmax   ymax  confidence  class    name
# 0  749.50   43.50  1148.0  704.5    0.874023      0  person
# 2  114.75  195.75  1095.0  708.0    0.624512      0  person
# 3  986.00  304.00  1028.0  420.0    0.286865     27     tie