drop materialized view group_transition_with_path;

create materialized view group_transition_with_path as
  with recursive group_tree as (
    select group_transition.group_transition_id,
        group_transition.group_generation_id,
        group_transition.group_name,
        group_transition.parent_group_transition_id,
        1 as layer,
        group_transition.group_transition_id::text as path,
        group_transition.group_name::text as path_name
      from group_transition
      where group_transition.parent_group_transition_id is null
    union all
    select group_transition.group_transition_id,
        group_transition.group_generation_id,
        group_transition.group_name,
        group_transition.parent_group_transition_id,
        group_tree.layer + 1 as layer,
        group_tree.path || E'\t' || group_transition.group_transition_id as path,
        group_tree.path_name || E'\t' || group_transition.group_name as path_name
      from group_tree
        join group_transition on group_transition.parent_group_transition_id = group_tree.group_transition_id
	  		and ((group_transition.group_generation_id = group_tree.group_generation_id)
				 or (group_tree.group_generation_id = 0))
  )
  select * from group_tree;

refresh materialized view group_transition_with_path;