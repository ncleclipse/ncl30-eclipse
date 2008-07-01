--[[
******************************************************************************
Este arquivo � parte da implementa��o do ambiente declarativo do middleware 
Ginga (Ginga-NCL).

Direitos Autorais Reservados (c) 1989-2007 PUC-Rio/Laborat�rio TeleM�dia

Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob 
os termos da Licen�a P�blica Geral GNU vers�o 2 conforme publicada pela Free 
Software Foundation.

Este programa � distribu�do na expectativa de que seja �til, por�m, SEM 
NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU 
ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral do 
GNU vers�o 2 para mais detalhes. 

Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do GNU vers�o 2 junto 
com este programa; se n�o, escreva para a Free Software Foundation, Inc., no 
endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA. 

Para maiores informa��es:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
******************************************************************************
This file is part of the declarative environment of middleware Ginga (Ginga-NCL)

Copyright: 1989-2007 PUC-RIO/LABORATORIO TELEMIDIA, All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
ncl @ telemidia.puc-rio.br
http://www.ncl.org.br
http://www.ginga.org.br
http://www.telemidia.puc-rio.br
*******************************************************************************
]]--

if mainCanvas == nil then
    print("Error. mainCanvas object is nil")
    return
end

canvas = {java_class = mainCanvas}
canvas.__index = canvas

_G.mainCanvas = nil

function canvas:createNew()
    local o = {}   -- create object if user does not provide one
    setmetatable(o, self)
    self.__index = self
    return o
end

function canvas:new(arg1,arg2)
    local newCanvas = canvas:createNew()
    if(arg2 == nil) then
        local image_path = arg1
        --newCanvas:setJavaClass(luajava.newInstance("br.pucrio.telemidia.ginga.core.player.procedural.lua.LuaCanvas", image_path))
        newCanvas:setJavaClass(self.java_class:createNew(image_path))
    else
        local width = arg1
        local height = arg2
        --newCanvas:setJavaClass(luajava.newInstance("br.pucrio.telemidia.ginga.core.player.procedural.lua.LuaCanvas",width,height))
        newCanvas:setJavaClass(self.java_class:createNew(width,height))
    end
    return newCanvas
end

function canvas:setJavaClass(new_java_class)
    self.java_class = new_java_class
end

function canvas:attrSize(w,h)
    if(w == nil and h == nil) then
        local dim = self.java_class:getSize()
        return dim.width,dim.height
    else
        self.java_class:setSize(w,h)
    end
end

function canvas:attrColor(r,g,b,a)
    if r == nil and g == nil and a == nil and a == nil then
        local color = self.java_class:getColor()
        return color:getRed(), color:getGreen(), color:getBlue(), color:getAlpha()
    elseif r ~= nil and g == nil and a == nil and a == nil then
        self.java_class:setColor(r)
    else
        self.java_class:setColor(r,g,b,a)
    end
end

function canvas:attrFont(face,size,style)
    if face==nil and size == nil and style == nil then
        local font = self.java_class:getFont()
        local font_name = font:getFontName()
        local font_size = font:getSize()
        local aux_style = font:getStyle()
        local font_style = nil
        if aux_style==font.BOLD then
            font_style="bold"
        elseif aux_style==font.ITALIC then
            font_style = "italic"
        elseif aux_style==(font.ITALIC + font.BOLD) then
            font_style="bold-italic"
        end
        return font_name,font_size,font_style
    else
        self.java_class:setFont(face,size,style)
    end
end


function canvas:attrClip (x, y, w, h)
	if(x == nil and y == nil and w == nil and h == nil) then
		local rect = self.java_class:getClip();
		return rect.x, rect.y, rect.width, rect.height
	else
		self.java_class:setClip(x,y,w,h)
	end
end

function canvas:pixel (x, y, r, g, b, a)
    if(r == nil and g == nil and b == nil and a == nil) then
        local color = self.java_class:getPixelValue(x,y)
        return color:getRed(), color:getGreen(), color:getBlue()
    else
        self.java_class:drawPixel(x,y,r,g,b,a)
    end
end


function canvas:drawLine (x1, y1, x2, y2)
    self.java_class:drawLine(x1, y1, x2, y2)
end

function canvas:drawRect (mode,x, y, w, h)
    if mode == "fill" then
        self.java_class:fillRect(x, y, w, h)
    else
        self.java_class:drawRect(x, y, w, h)
    end
end

function canvas:fillRect (x, y, w, h)
    self.java_class:fillRect(x, y, w, h)
end

function canvas:drawText (x, y, text)
    self.java_class:drawText(x,y,text)
end

function canvas:drawPolygon (mode)
    local i=0
    local pointsX
    local pointsY
    local drawFunction = function(x,y)
    	if(x ~= nil and y ~= nil) then
            pointsX[i] = x
            pointsY[i] = y
            i = i+1
    	    return drawFunction(x,y)
        else
            self.java_class:drawPolygon(pointsX,pointsY,mode)
    	end
    end
end

function canvas:drawEllipse(mode, xc, yc, width, height, ang_start, ang_end)
    if(mode == "fill") then
        self.java_class:fillEllipse(xc,yc,width,height, ang_start, ang_end)
    end
    
    if(mode == "arc") then
        self.java_class:drawEllipse(xc,yc,width,height, ang_start, ang_end)
    end
end

function canvas:measureText(text)
    if text == nil then
        return 0,0
    else
        local rect = self.java_class:measureText(text)
        return rect.width, rect.height
    end
end

function canvas:flush ()
    self.java_class:flush()
end

function canvas:compose(x, y, src)
    self.java_class:compose(x,y,src.java_class)
end
