function [albedo_image, surface_normals] = photometric_stereo(imarray, light_dirs)
% imarray: h x w x Nimages array of Nimages no. of images
% light_dirs: Nimages x 3 array of light source directions
% albedo_image: h x w image
% surface_normals: h x w x 3 array of unit surface normals


%% <<< fill in your code below >>>
%%imshow(imarray);
[h w Nimages] = size(imarray);
G = zeros(1,3);
surfaceNormal = zeros(1,3);
surface_normals = zeros(h,w,3);
albedo_image = zeros(h,w);

intensity = zeros(64,1);
for i = 1 : h;
    for j = 1 : w;
        %for k = 1 : Nimages
            intensity(:,1)  = imarray(i,j,:); % Storing image intensity in an array of intensity
            G(1,:) = light_dirs\intensity;      % calculating the vale of G in usng the light_dirs and intensity
            albedo_image(i,j) = norm(G,3);      % calculating the albedo if each pixel and storing in an array
            surfaceNormal = G/albedo_image(i,j); % calculating the surface normal of each pixel
            surface_normals(i,j,:) = surfaceNormal; % Storing the surface normal of each pixel in a 3D array
            
    end
end

end

