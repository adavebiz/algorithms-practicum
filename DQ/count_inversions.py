
def count(list,start, end):

	if (end-start<=0):
		return 0	

	mid = (start+end)/2
	return count(list, start , mid) + count(list, mid +1, end) + merge_count(list,start,mid,mid+1,end)

	

def merge_count(list, startX, endX, startY, endY):

	aux_sort_list = []
	inv_count_list =[]
	x = startX
	y = startY
	inv_count = 0
	n_y_copied = 0

	while(x<=endX and y<=endY):
		if(list[x]<list[y]):
			aux_sort_list.append(list[x])
			x = x+1
			inv_count = inv_count + n_y_copied
		else:
			aux_sort_list.append(list[y])
			y = y+1
			n_y_copied = n_y_copied + 1
			# here an inversion is encountered	
			

	while(x<=endX):	
		aux_sort_list.append(list[x])
		inv_count = inv_count + n_y_copied
		x = x+1

	while(y<=endY):
		aux_sort_list.append(list[y])
		n_y_copied = n_y_copied + 1 # though this will not beused any further
		y=y+1

	k=0
	for i in range(startX,endY+1):
		list[i] = aux_sort_list[k]	
		k=k+1

	return (inv_count)	

def read_int():
	return int(raw_input())

# main code starts here
t = read_int()

while (t>0):
	
	blank = raw_input() # blank
	items = read_int()
	list = []
	while (items>0):
		list.append(read_int())
		items = items-1

	# print list
	print (count(list,0,len(list)-1))
	t = t-1
