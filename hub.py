import torch
import os
import sys
from time import time
from DeepImageSearch import Load_Data, Search_Setup
import shutil

# start = time()
arg = sys.argv
home_path = os.path.expanduser('~')
# model = torch.hub.load('ultralytics/yolov5', 'custom', path='/Users/kimjungi/back/eunhy/yolov5/best.pt')
model = torch.hub.load('twinson37/yolov5', 'custom', path= f'{home_path}/MismatchedSocks/best.pt')  
# model = torch.hub.load('twinson37/yolov5', 'custom', path= f'{home_path}/MismatchedSocks/best.pt', source='local')  

# Images
# im = '/Users/kimjungi/KakaoTalk_Photo_2023-01-31-11-04-51 021.jpeg'  # or file, Path, URL, PIL, OpenCV, numpy, list
im = f'{home_path}/img/{arg[1]}'

# Inference
results = model(im)

# Results
results.save()  # or .show(), .save(), .crop(), .pandas(), etc.
results.crop()  # or .show(), .save(), .crop(), .pandas(), etc.

# results.show()

results.xyxy[0]  # im predictions (tensor)
results.pandas().xyxy[0]  # im predictions (pandas)
#      xmin    ymin    xmax   ymax  confidence  class    name
# 0  749.50   43.50  1148.0  704.5    0.874023      0  person
# 2  114.75  195.75  1095.0  708.0    0.624512      0  person
# 3  986.00  304.00  1028.0  420.0    0.286865     27     tie


# metadata = st.get_image_metadata_file()
# metadata = st.get_image_metadata_file()

print(results.pandas().xyxy[0])


img_path = os.path.join(home_path,"runs/detect/exp/crops/sock/")
meta_path = os.path.join(home_path,"metadata-files")

# dl = Load_Data()
# image_list = dl.from_folder([img_path])
#
# num = len(image_list)
#
# mathced = list()
# 발견 양말 없을 때 하나일 때 두개일때?
# dirListing = os.listdir(img_path)

# for _ in range(num):
#     if(os.path.isdir(meta_path)):
#         shutil.rmtree(meta_path)
#
#     st = Search_Setup(image_list, model_name="vgg19", pretrained=True, image_count=None)
#     st.run_index()
#     # print(st.get_similar_images(image_path=image_list[0], number_of_images=2))
#     temp = st.get_similar_images(image_path=image_list[0], number_of_images=2)
#     temp.pop(0)
#
#     mathced.append([image_list[0],list(temp.values())[0]])
#
#     image_list.remove(image_list[0])
#     image_list.remove(list(temp.values())[0])
#
#     # print(image_list)
#     new_num = len(image_list)
#     if (new_num==0) or (new_num==1):
#         break;
#
# print(mathced)

# print(f"hub = {time() -start}")