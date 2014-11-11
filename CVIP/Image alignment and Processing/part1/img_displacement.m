function [img_adjust] = img_displacement(imageOne,imageTwo)
% finds the best adjustment as per the minimum value and returns a pixel
% value for which the best possible
% alignment of images is done.
min = inf;   % sets the value to infinity so that the first value of displacement is saved as the initial minimum value

for i = -15:15                  % for searching the entire image for displacement vector
    for j = -15:15
        adj_value = circshift(imageOne,[i,j]); % shifting one image as per the pixel values for alingment
        ssd_value = sum(sum((imageTwo - adj_value).^2)); % getting the SSD values for the displacement
        if ssd_value < min
            min = ssd_value;        % checking for the minimum displacement value
            img_adjust = [i,j];
        end
    end
end
end
