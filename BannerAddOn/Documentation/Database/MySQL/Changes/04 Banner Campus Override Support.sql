/*
 * UniTime 3.2 (University Timetabling Application)
 * Copyright (C) 2013, UniTime LLC
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
*/
/*
 * Author:  Stephanie Schluttenhofer
 */
 
/*
 * Add table to store Banner Campus Overrides
 */

create table banner_campus_override (
				uniqueid decimal(20,0) primary key not null,
				banner_campus_code varchar(20) not null,
				banner_campus_name varchar(100) not null,
				visible decimal(1,0) not null) engine = INNODB;
    
/*
 * Add column to BannerSection to store banner campus override
 */
 
alter table banner_section add banner_campus_override_id decimal(20,0);
alter table banner_section add constraint fk_campus_override foreign key (banner_campus_override_id) references banner_campus_override(uniqueid) on delete set null;
 
 
/*
 * Update database version
 */

update application_config set value='4' where name='tmtbl.db.banner.version';


commit;