function [aligned_image] = alignImage(imageOne, imageTwo)
img_adjust = zeros(1,2);

img_adjust = img_displacement(imageOne, imageTwo); % obtaining the minimum displacement for the best possible alignment
aligned_image = circshift(imageOne,img_adjust);    % shifting the image as per the alignment so that it gets aligned properly


end