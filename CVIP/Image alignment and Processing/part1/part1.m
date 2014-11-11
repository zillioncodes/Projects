% CSE 473/573 Programming Assignment 1, starter Matlab code
% Adapted from A. Efros
% (http://graphics.cs.cmu.edu/courses/15-463/2010_fall/hw/proj1/)
% and R. Fergus
% http://cs.nyu.edu/~fergus/teaching/vision/assign1.pdf
path = 'part1_6.jpg';
% name of the input file
imname = path;

% read in the image
fullim = imread(imname);

% convert to double matrix 
fullim = im2double(fullim);

% compute the height of each part (just 1/3 of total)
height = floor(size(fullim,1)/3);
% separate color channels
B = fullim(1:height,:);
G = fullim(height+1:height*2,:);
R = fullim(height*2+1:height*3,:);

% Align the images
% Functions that might be useful to you for aligning the images include: 
% "circshift", "sum"
aG = alignImage(G,B);  % aligning of G image as per B 

aR = alignImage(R,B);  % aligning of R image as per B

% open figure
%% figure(1);

% create a color image (3D array)
% ... use the "cat" command
resultImage = cat(3,aR,aG,B); % concatenating all the three images
imshow(resultImage); % displaying the resultant image

% show the resulting image
% ... use the "imshow" command

% save result image
imwrite(resultImage,sprintf('%s_result', path), '.jpg'); % saving the result image


