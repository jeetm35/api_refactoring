
# coding: utf-8

# In[5]:


import os


# In[20]:


dst = '/Users/shreya/eclipse/Workspace/InstrumentationHelper/junit'
if not os.path.exists(dst):
    os.mkdir(dst)
API = 'junit4-r4.12' 
API_new = 'junit4-r4.12_new2' 


# In[21]:


#      myPackage.myXstreamClass.printFile(myPackage.myXstreamClass.getXstream().toXML(condition),"myFilePath.txt");
iline_1 = 'myPackage.myXstreamClass.printFile(myPackage.myXstreamClass.getXstream().toXML(*),^);'
print(iline_1.replace('*','word').replace('^','myfilepath'))


# In[22]:


cur_src_file_path = None
cur_src_file = None
dst_file_path = None
dst_file = None

with open("parsedDataMini.txt", "r") as changeFile:
    for line in changeFile:
        print(line)
        src_file_path, function, line_no , parameters , returnType = line.split('|')
        
        if ((cur_src_file_path == None) or (src_file_path != cur_src_file_path)):
                
                if(cur_src_file_path != None):
                   
                    dst_file.write(cur_src_file.read())
                    
                    cur_src_file.close()
                    dst_file.close()
                
        
                cur_src_file_path = src_file_path
                
                dst_file_path = cur_src_file_path.replace(API,API_new)
#                 print(dst_src_file_path)

                cur_src_file  = open(cur_src_file_path, 'r') 
                dst_file  = open(dst_file_path, 'w')
        
                line_count = 0
                
            
        while(line_count < int(line_no)):
#             print(line_no,line_count)
            src_line = cur_src_file.readline()
#             print(src_line)
            dst_file.write(src_line)
            line_count+=1
            
        if(len(parameters)>2):
            dst_file.write('\n')
            dst_file.write('/*-------------------Instrumentation Start-------------------*/ \n')
            dst_file.write('\n')
            
            for s in  parameters[1:-1].split(','):
                file_path = function+'_'+s+'_'+returnType
                print(file_path.strip('\n'))
                dst_file.write(iline_1.replace('*',s).replace('^',"\""+file_path.strip('\n')+"\""))
                dst_file.write('\n')
                print(file_path)
            dst_file.write('\n')
            dst_file.write('/*-------------------Instrumentation Finish-------------------*/ \n')
            dst_file.write('\n')
            
        

# Windup              
dst_file.write(cur_src_file.read())                
cur_src_file.close()
dst_file.close()  
changeFile.close()
                
         
            
         
            
        
        
        


# In[47]:


parameters = '(ab,b)'
'_'.join(parameters[1:-1].split(','))


# In[40]:


print('|'.join('a'+'b'))


# In[19]:


cur_src_file_path


# In[56]:


file_path


# In[57]:


returnType

