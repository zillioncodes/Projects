function  height_map = get_surface(surface_normals, image_size)
% surface_normals: h x w x 3 array of unit surface normals
% image_size: [h, w] of output height map/image
% height_map: height map of object of dimensions [h, w]


h = image_size(1,1); 
w = image_size(1,2);
height_map = zeros(h,w);
height_map(1,1) = 0;
%% <<< fill in your code below >>> 
for i = 2: h;
    N1 = surface_normals(i,1,1);
    N3 = surface_normals(i,1,3);
    q= N1/N3;
    height_map(i,1) = height_map(i-1,1) + q; 
end
for i = 1: h;
    for j =2: w;
        N2 = surface_normals(i,j,2);
        N3 = surface_normals(i,j,3);
        p= N2/N3;
        height_value_rest = height_map(i,j-1) + p;
        height_map(i,j) = height_value_rest; 
    end
end


