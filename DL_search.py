from DeepImageSearch import Load_Data, Search_Setup
import os
import shutil
from time import time


start = time()
home_path = os.path.expanduser('~')
img_path = os.path.join(home_path,"runs/detect/exp/crops/sock/")
meta_path = os.path.join(home_path,"metadata-files")

os.chdir(home_path)

dl = Load_Data()
image_list = dl.from_folder([img_path])

num = len(image_list)

mathced = list()
for _ in range(num):
    shutil.rmtree(meta_path)

    st = Search_Setup(image_list, model_name="vgg19", pretrained=True, image_count=None)
    st.run_index()
    print(st.get_similar_images(image_path=image_list[0], number_of_images=2))
    temp = st.get_similar_images(image_path=image_list[0], number_of_images=2)
    temp.pop(0)

    mathced.append([image_list[0],list(temp.values())[0]])

    image_list.remove(image_list[0])
    image_list.remove(list(temp.values())[0])
    
    # print(image_list)
    new_num = len(image_list)
    if (new_num==0) or (new_num==1): 
        break;

print(mathced)
print(f"DL_seatch = {time() -start}")
# 22.262634992599487