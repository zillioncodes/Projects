function resultImage = segmentImg(I, k)
% function idx = segmentImage(img)
% Returns the logical image containing the segment ids obtained from 
%   segmenting the input image
%
% INPUTS
% I - The input image contining textured foreground objects to be segmented
%     out.
% k - The number of segments to compute (also the k-means parameter).
%
% OUTPUTS
% idx - The logical image (same dimensions as the input image) contining 
%       the segment ids after segmentation. The maximum value of idx is k.
%          

    % 1. Create your bank of filters using the given alogrithm; 
    % 2. Compute the filter responses by convolving your input image with  
    %     each of the num_filters in the bank of filters F.
    %     responses(:,:,i)=conv2(I,F(:,:,i),'same')
    %     NOTE: we suggest to use 'same' instead of 'full' or 'valid'.
    % 3. Remember to take the absolute value of the filter responses (no
    %     negative values should be used).
    % 4. Construct a matrix X of the points to be clustered, where 
    %     the rows of X = the total number of pixels in I (rows*cols); and
    %     the columns of X = num_filters;
    %     i.e. each pixel is transformed into a num_filters-dimensional
    %     vector.
    % 5. Run kmeans to cluster the pixel features into k clusters,
    %     returning a vector IDX of labels.
    % 6. Reshape IDX into an image with same dimensionality as I and return
    %     the reshaped index image.
    %
    file = 'cheetah.jpg';
    I = imread(file);
    I = im2double(I);
    [h w] = size(I);
    numOfPixels = h*w;
    imshow(I);
    I = double(rgb2gray(I)); I=I(:,:,1);
    [rows cols] = size(I);
    numOfPixels1 = rows*cols;
    F=makeLMfilters;
    [~,~,num_filters] = size(F);
    X = zeros(numOfPixels1,num_filters);
    responses = zeros(num_filters, rows, cols);
    for i = 1:numOfPixels1;
        for p = 1:size(num_filters)
            response =conv2(I,F(:,:,p),'same');
            response = abs(response);
            responses(p,:,:) = response;
        end
    end
    %imshow(responses); 
    %for i = 1:numOfPixels1;
       %     for k = 1: rows;
        %       for m = 1:cols;
         %              for j = 1:num_filters;
          %                     X(i,j) = responses(j,k,m);
           %            end
            %   end
            %end
    %end
    k = 2;
    %imshow(X);
    %[idx, C, sumD, D] = kmeans(X, k);
    %resultImage = reshape(idx,[h,w]);
    %imshow(resultImage);
    
end
